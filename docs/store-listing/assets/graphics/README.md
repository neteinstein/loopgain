# Store graphics — how these were generated

All three scripts here are generated, not hand-drawn, from the app's own brand source, and all
render at 4x resolution before downsampling with LANCZOS — PIL's line/ellipse/text drawing
primitives are aliased on their own, so this is what keeps the mark's curves smooth instead of
jagged:

- `generate_icon.py` → `icon_512.png` (512×512 hi-res icon for Play Console). Rasterizes the
  exact path data and linear gradients from
  `androidApp/src/main/res/drawable/ic_launcher_foreground.xml` and
  `ic_launcher_background.xml` at full resolution, rather than upscaling the small
  (max 192×192) launcher PNGs in `androidApp/src/main/res/mipmap-*`.
- `generate_feature_graphic.py` → `feature_graphic_1024x500.png` (Play Console feature
  graphic). Same mark and the loading-screen's blue gradient and tagline (`LoadingScreen.kt`)
  on the left; on the right, a fanned hand of four cards using the exact category colors and
  label tints from `CardStyles.kt` (`CardColors.Motto`/`PositiveReinforcement`/`Improvements`/
  `PersonalQuestion`), each with real deck question text, to make the graphic read as "a card
  deck app" at a glance instead of a plain logo lockup.
- `generate_launcher_mipmaps.py` → the app's *own* legacy-fallback icons, writing directly into
  `androidApp/src/main/res/mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/ic_launcher.png` and
  `ic_launcher_round.png`. Same source vector, same anti-aliasing, at each required density.
  `mipmap-anydpi-v26/*.xml` (what API26+ actually renders) is untouched — it's already a vector
  and needs no regeneration; this only matters for older API levels and tooling that fall back
  to the static PNGs.

Re-run any script after the launcher icon or brand colors change, from inside this directory —
each writes its output next to itself (`generate_launcher_mipmaps.py` writes into the app's
`res/mipmap-*` instead, several directories up). All three need Pillow (`pip install pillow`)
and, for the feature graphic, macOS's bundled Arial fonts
(`/System/Library/Fonts/Supplemental/Arial*.ttf`); swap the font paths if generating on a
different OS.
