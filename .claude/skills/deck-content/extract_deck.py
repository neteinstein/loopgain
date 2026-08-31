#!/usr/bin/env python3
"""Extract the LoopGain question deck from docs/LoopGain - Questions.xlsx.

Uses only the Python standard library: an .xlsx is a zip of XML, and the
container this repo is usually worked on from has no openpyxl.

    python3 .claude/skills/deck-content/extract_deck.py            # summary + invariant check
    python3 .claude/skills/deck-content/extract_deck.py --tsv      # id/category/level/en/pt rows
    python3 .claude/skills/deck-content/extract_deck.py --kotlin   # BundledDeck.kt entries
    python3 .claude/skills/deck-content/extract_deck.py --guide    # Intro/Instructions/Back-of-deck copy

Exit code is 1 if any deck invariant fails, so it doubles as a check.
"""

import argparse
import re
import sys
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path

WORKBOOK = Path("docs/LoopGain - Questions.xlsx")
SHEET = "Teams Cards v2"

NS = "{http://schemas.openxmlformats.org/spreadsheetml/2006/main}"
REL_NS = "{http://schemas.openxmlformats.org/officeDocument/2006/relationships}"
PKG_REL_NS = "{http://schemas.openxmlformats.org/package/2006/relationships}"

# Category header cells look like "Positive Reinforcement *" — the trailing
# stars are the level. Motto has no stars and no level.
CATEGORIES = {
    "Motto": "MOTTO",
    "Positive Reinforcement": "POSITIVE_REINFORCEMENT",
    "Improvements": "IMPROVEMENTS",
    "Personal Question": "PERSONAL_QUESTION",
}
GUIDE_ROWS = {"Intro", "Instructions", "Back of the Deck"}


def sheet_path(zf, name):
    """Resolve a sheet name to its XML part, via workbook rels (never assume sheetN.xml)."""
    rels = {
        r.get("Id"): r.get("Target")
        for r in ET.fromstring(zf.read("xl/_rels/workbook.xml.rels")).iter(PKG_REL_NS + "Relationship")
    }
    for sheet in ET.fromstring(zf.read("xl/workbook.xml")).iter(NS + "sheet"):
        if sheet.get("name") == name:
            target = rels[sheet.get(REL_NS + "id")].lstrip("/")
            return target if target.startswith("xl/") else "xl/" + target
    raise SystemExit(f"sheet {name!r} not found in {WORKBOOK}")


def rows(path=WORKBOOK, sheet=SHEET):
    """Yield {column letter: text} per row, in sheet order."""
    with zipfile.ZipFile(path) as zf:
        shared = [
            "".join(t.text or "" for t in si.iter(NS + "t"))
            for si in ET.fromstring(zf.read("xl/sharedStrings.xml"))
        ]
        for row in ET.fromstring(zf.read(sheet_path(zf, sheet))).iter(NS + "row"):
            cells = {}
            for c in row.iter(NS + "c"):
                col = re.match(r"[A-Z]+", c.get("r")).group()
                v = c.find(NS + "v")
                if v is None or v.text is None:
                    continue
                cells[col] = shared[int(v.text)] if c.get("t") == "s" else v.text
            if cells:
                yield cells


def parse():
    """Return (cards, guide). Cards carry a forward-filled category and level."""
    cards, guide = [], []
    category, level = None, None
    for cells in rows():
        header = (cells.get("B") or "").strip()
        if header in GUIDE_ROWS:
            guide.append({"title": header, "en": cells.get("C", ""), "pt": cells.get("D", "")})
            continue
        if header:
            base = header.rstrip("* ").strip()
            if base in CATEGORIES:
                # The header only appears on the first row of each block; it
                # applies until the next one.
                category = CATEGORIES[base]
                stars = header.count("*")
                level = stars or None
        en, pt = (cells.get("C") or "").strip(), (cells.get("D") or "").strip()
        if not category or not en:
            continue
        seq = sum(1 for c in cards if c["category"] == category) + 1
        cards.append(
            {"id": f"{category.lower()}_{seq:02d}", "category": category, "level": level, "en": en, "pt": pt}
        )
    return cards, guide


def check(cards):
    """Deck invariants. Any failure here means the extraction, not the deck, is wrong."""
    problems = []
    if len(cards) != 48:
        problems.append(f"expected 48 cards, got {len(cards)}")
    for name in CATEGORIES.values():
        block = [c for c in cards if c["category"] == name]
        if len(block) != 12:
            problems.append(f"{name}: expected 12 cards, got {len(block)}")
        if name == "MOTTO":
            if any(c["level"] is not None for c in block):
                problems.append("MOTTO cards must have no level")
        else:
            for lvl in (1, 2, 3):
                n = sum(1 for c in block if c["level"] == lvl)
                if n != 4:
                    problems.append(f"{name} level {lvl}: expected 4 cards, got {n}")
    if len({c["id"] for c in cards}) != len(cards):
        problems.append("card ids are not unique")
    for c in cards:
        if not c["en"] or not c["pt"]:
            problems.append(f"{c['id']} is missing EN or PT text")
    return problems


def kotlin(cards):
    def esc(s):
        return s.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$")

    for c in cards:
        lvl = f"CardLevel.{('ONE', 'TWO', 'THREE')[c['level'] - 1]}" if c["level"] else "null"
        yield (
            f'    QuestionCard("{c["id"]}", CardCategory.{c["category"]}, {lvl},\n'
            f'        en = "{esc(c["en"])}",\n'
            f'        pt = "{esc(c["pt"])}"),'
        )


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--tsv", action="store_true")
    ap.add_argument("--kotlin", action="store_true")
    ap.add_argument("--guide", action="store_true")
    args = ap.parse_args()

    cards, guide = parse()

    if args.tsv:
        for c in cards:
            print("\t".join([c["id"], c["category"], str(c["level"] or ""), c["en"], c["pt"]]))
    elif args.kotlin:
        for line in kotlin(cards):
            print(line)
    elif args.guide:
        for g in guide:
            print(f"## {g['title']}\n\nEN: {g['en']}\n\nPT: {g['pt']}\n")
    else:
        for name in CATEGORIES.values():
            block = [c for c in cards if c["category"] == name]
            counts = {lvl: sum(1 for c in block if c["level"] == lvl) for lvl in (1, 2, 3)}
            levels = ", ".join(f"L{lvl}={n}" for lvl, n in counts.items() if n) or "no levels"
            print(f"{name:<24} {len(block):>2} cards  ({levels})")
        print(f"{'TOTAL':<24} {len(cards):>2} cards, {len(guide)} guide rows")

    problems = check(cards)
    for p in problems:
        print(f"FAIL: {p}", file=sys.stderr)
    return 1 if problems else 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except BrokenPipeError:  # piping into head is normal here
        sys.exit(0)
