import re
import math
from PIL import Image, ImageDraw, ImageFont

W, H = 1024, 500
VIEWPORT = 108.0

ARIAL = "/System/Library/Fonts/Supplemental/Arial.ttf"
ARIAL_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"

RIGHT_PATH = "M54.13,53.88 L54.66,53.35 L55.31,52.69 L55.98,52.04 L56.66,51.40 L57.35,50.77 L58.06,50.14 L58.79,49.53 L59.54,48.94 L60.33,48.36 L61.14,47.81 L61.99,47.30 L62.88,46.81 L63.81,46.38 L64.78,45.99 L65.79,45.67 L66.83,45.41 L67.92,45.24 L69.03,45.16 L70.17,45.19 L71.32,45.34 L72.47,45.62 L73.60,46.03 L74.68,46.59 L75.70,47.29 L76.63,48.14 L77.44,49.13 L78.10,50.23 L78.59,51.43 L78.90,52.70 L79.00,54.00 L78.90,55.30 L78.59,56.57 L78.10,57.77 L77.44,58.87 L76.63,59.86 L75.70,60.71 L74.68,61.41 L73.60,61.97 L72.47,62.38 L71.32,62.66 L70.17,62.81 L69.03,62.84 L67.92,62.76 L66.83,62.59 L65.79,62.33 L64.78,62.01 L63.81,61.62 L62.88,61.19 L61.99,60.70 L61.14,60.19 L60.33,59.64 L59.54,59.06 L58.79,58.47 L58.06,57.86 L57.35,57.23 L56.66,56.60 L55.98,55.96 L55.31,55.31 L54.66,54.65 L54.13,54.12"
RIGHT_GRAD = dict(sx=58, sy=47, ex=76, ey=61, sc=(0x5B, 0xB8, 0xE8), ec=(0x16, 0x3A, 0x66))
LEFT_PATH = "M53.87,53.88 L53.34,53.35 L52.69,52.69 L52.02,52.04 L51.34,51.40 L50.65,50.77 L49.94,50.14 L49.21,49.53 L48.46,48.94 L47.67,48.36 L46.86,47.81 L46.01,47.30 L45.12,46.81 L44.19,46.38 L43.22,45.99 L42.21,45.67 L41.17,45.41 L40.08,45.24 L38.97,45.16 L37.83,45.19 L36.68,45.34 L35.53,45.62 L34.40,46.03 L33.32,46.59 L32.30,47.29 L31.37,48.14 L30.56,49.13 L29.90,50.23 L29.41,51.43 L29.10,52.70 L29.00,54.00 L29.10,55.30 L29.41,56.57 L29.90,57.77 L30.56,58.87 L31.37,59.86 L32.30,60.71 L33.32,61.41 L34.40,61.97 L35.53,62.38 L36.68,62.66 L37.83,62.81 L38.97,62.84 L40.08,62.76 L41.17,62.59 L42.21,62.33 L43.22,62.01 L44.19,61.62 L45.12,61.19 L46.01,60.70 L46.86,60.19 L47.67,59.64 L48.46,59.06 L49.21,58.47 L49.94,57.86 L50.65,57.23 L51.34,56.60 L52.02,55.96 L52.69,55.31 L53.34,54.65 L53.87,54.12"
LEFT_GRAD = dict(sx=32, sy=47, ex=50, ey=61, sc=(0xFF, 0xFF, 0xFF), ec=(0xC8, 0xDF, 0xEF))


def parse_path(d):
    nums = re.findall(r"-?\d+\.?\d*", d)
    coords = list(map(float, nums))
    return [(coords[i], coords[i + 1]) for i in range(0, len(coords), 2)]


def lerp(a, b, t):
    return tuple(round(a[i] + (b[i] - a[i]) * t) for i in range(3))


def grad_color(x, y, g):
    dx, dy = g["ex"] - g["sx"], g["ey"] - g["sy"]
    denom = dx * dx + dy * dy
    t = ((x - g["sx"]) * dx + (y - g["sy"]) * dy) / denom
    t = max(0.0, min(1.0, t))
    return lerp(g["sc"], g["ec"], t)


ALL_PTS = parse_path(RIGHT_PATH) + parse_path(LEFT_PATH)
BBOX_MIN_X = min(p[0] for p in ALL_PTS)
BBOX_MIN_Y = min(p[1] for p in ALL_PTS)
BBOX_MAX_X = max(p[0] for p in ALL_PTS)
BBOX_MAX_Y = max(p[1] for p in ALL_PTS)
BBOX_W = BBOX_MAX_X - BBOX_MIN_X
BBOX_H = BBOX_MAX_Y - BBOX_MIN_Y


def draw_mark(draw, ox, oy, scale, stroke_units=13.0):
    stroke = stroke_units * scale
    pad = stroke / 2

    def tx(x, y):
        return ox + pad + (x - BBOX_MIN_X) * scale, oy + pad + (y - BBOX_MIN_Y) * scale

    for path_d, grad in ((RIGHT_PATH, RIGHT_GRAD), (LEFT_PATH, LEFT_GRAD)):
        pts = parse_path(path_d)
        for i in range(len(pts) - 1):
            x0, y0 = pts[i]
            x1, y1 = pts[i + 1]
            c0 = grad_color(x0, y0, grad)
            c1 = grad_color(x1, y1, grad)
            c = tuple((c0[k] + c1[k]) // 2 for k in range(3))
            p0, p1 = tx(x0, y0), tx(x1, y1)
            draw.line([p0, p1], fill=c + (255,), width=round(stroke), joint="curve")
            r = stroke / 2
            draw.ellipse([p0[0] - r, p0[1] - r, p0[0] + r, p0[1] + r], fill=c0 + (255,))
        xl, yl = pts[-1]
        cl = grad_color(xl, yl, grad)
        pl = tx(xl, yl)
        r = stroke / 2
        draw.ellipse([pl[0] - r, pl[1] - r, pl[0] + r, pl[1] + r], fill=cl + (255,))


def wrap_text(draw, text, font, max_width):
    words = text.split()
    lines = []
    cur = ""
    for w in words:
        trial = (cur + " " + w).strip()
        if draw.textbbox((0, 0), trial, font=font)[2] <= max_width:
            cur = trial
        else:
            if cur:
                lines.append(cur)
            cur = w
    if cur:
        lines.append(cur)
    return lines


# ---- one printed-deck card, as its own RGBA sprite (with room to rotate) ----
CARD_W, CARD_H = 178, 246
CARD_RADIUS = 16


def make_card(bg, fg, label_color, label, question, dots=None):
    pad_canvas = 60  # room for rotation without clipping
    size = (CARD_W + pad_canvas * 2, CARD_H + pad_canvas * 2)
    sprite = Image.new("RGBA", size, (0, 0, 0, 0))
    d = ImageDraw.Draw(sprite)
    x0, y0 = pad_canvas, pad_canvas
    x1, y1 = x0 + CARD_W, y0 + CARD_H

    # soft shadow
    shadow = Image.new("RGBA", size, (0, 0, 0, 0))
    sd = ImageDraw.Draw(shadow)
    sd.rounded_rectangle([x0 + 6, y0 + 10, x1 + 6, y1 + 10], radius=CARD_RADIUS, fill=(0, 0, 0, 90))
    shadow = shadow.filter(__import__("PIL.ImageFilter", fromlist=["ImageFilter"]).GaussianBlur(8))
    sprite = Image.alpha_composite(shadow, sprite)
    d = ImageDraw.Draw(sprite)

    d.rounded_rectangle([x0, y0, x1, y1], radius=CARD_RADIUS, fill=bg + (255,))

    font_label = ImageFont.truetype(ARIAL_BOLD, 13)
    d.text((x0 + 16, y0 + 17), label.upper(), font=font_label, fill=label_color + (255,))

    if dots is not None:
        r = 3.5
        gap = 11
        dy = y0 + 22
        for i in range(3):
            color = label_color if i < dots else (label_color[0], label_color[1], label_color[2], 90)
            cx = x1 - 16 - (2 - i) * gap
            fillc = color if len(color) == 4 else color + (255,)
            d.ellipse([cx - r, dy - r, cx + r, dy + r], fill=fillc)

    font_q = ImageFont.truetype(ARIAL_BOLD, 18)
    lines = wrap_text(d, question, font_q, CARD_W - 32)
    line_h = 23
    text_block_h = line_h * len(lines)
    ty = y0 + CARD_H / 2 - text_block_h / 2 + 6
    for line in lines:
        d.text((x0 + 16, ty), line, font=font_q, fill=fg + (255,))
        ty += line_h

    return sprite, pad_canvas


def paste_rotated(base, sprite, pad_canvas, center_xy, angle_deg):
    rotated = sprite.rotate(angle_deg, resample=Image.BICUBIC, expand=True)
    cx, cy = center_xy
    px = round(cx - rotated.width / 2)
    py = round(cy - rotated.height / 2)
    base.alpha_composite(rotated, (px, py))


NAVY = (0x0D, 0x22, 0x54)
WHITE = (255, 255, 255)
POS_BG = (0x5B, 0xB8, 0xE8)
IMP_BG = (0xA9, 0xD0, 0xE8)
PQ_BG = (0xC8, 0xDF, 0xEF)
TEXT_ON_LIGHT = (0x0D, 0x22, 0x54)
POS_LABEL = (0x12, 0x68, 0xA0)
IMP_LABEL = (0x1E, 0x5F, 0xA8)
PQ_LABEL = (0x3A, 0x6C, 0x9B)

# background vertical gradient, matches LoadingScreen (#1E3A5F -> #4A90E2)
top = (0x1E, 0x3A, 0x5F)
bottom = (0x4A, 0x90, 0xE2)
img = Image.new("RGBA", (W, H), (0, 0, 0, 255))
px = img.load()
for y in range(H):
    t = y / (H - 1)
    c = lerp(top, bottom, t)
    for x in range(W):
        px[x, y] = c + (255,)

draw = ImageDraw.Draw(img)

# ---- right side: a fanned hand of four printed cards ----
cards = [
    make_card(NAVY, WHITE, WHITE, "Motto", "Being honest does not mean being rude.", dots=None),
    make_card(POS_BG, TEXT_ON_LIGHT, POS_LABEL, "Positive",
              "3 things ___ does that I'd also like to do.", dots=2),
    make_card(IMP_BG, TEXT_ON_LIGHT, IMP_LABEL, "Improvements",
              "What can I help ___ improve? Why?", dots=2),
    make_card(PQ_BG, TEXT_ON_LIGHT, PQ_LABEL, "Personal",
              "What's the best thing that happened this month?", dots=2),
]

fan_center = (742, 300)
angles = [-16, -5.5, 5.5, 16]
offsets = [(-152, 26), (-51, 6), (51, 6), (152, 26)]
for (sprite, pad), angle, (ox, oy) in zip(cards, angles, offsets):
    paste_rotated(img, sprite, pad, (fan_center[0] + ox, fan_center[1] + oy), angle)

# ---- left side: mark + wordmark + tagline, left aligned ----
draw = ImageDraw.Draw(img)
mark_scale = 3.6
mark_w = (BBOX_W + 13.0) * mark_scale
mark_h = (BBOX_H + 13.0) * mark_scale

font_word = ImageFont.truetype(ARIAL_BOLD, 74)
word_text = "LoopGain"
wb = draw.textbbox((0, 0), word_text, font=font_word)
word_w = wb[2] - wb[0]
word_h = wb[3] - wb[1]

font_tag = ImageFont.truetype(ARIAL, 25)
tagline1 = "Structured feedback sessions"
tagline2 = "for teams — timed & kind."
tb1 = draw.textbbox((0, 0), tagline1, font=font_tag)
tb2 = draw.textbbox((0, 0), tagline2, font=font_tag)

left_x = 58
gap1, gap2, gap3 = 20, 22, 10
total_h = mark_h + gap1 + word_h + gap2 + (tb1[3] - tb1[1]) + gap3 + (tb2[3] - tb2[1])
top_y = (H - total_h) / 2 - 6

draw_mark(draw, left_x, top_y, mark_scale)

word_y = top_y + mark_h + gap1 - wb[1]
draw.text((left_x, word_y), word_text, font=font_word, fill=(0xE5, 0x34, 0x2F, 255))

tag_y1 = word_y + word_h + wb[1] + gap2 - tb1[1]
draw.text((left_x + 2, tag_y1), tagline1, font=font_tag, fill=(255, 255, 255, 235))
tag_y2 = tag_y1 + (tb1[3] - tb1[1]) + gap3 - tb2[1] + tb1[1]
draw.text((left_x + 2, tag_y2), tagline2, font=font_tag, fill=(255, 255, 255, 235))

import os
out_path = os.path.join(os.path.dirname(__file__), "feature_graphic_1024x500.png")
img.convert("RGB").save(out_path)
print("saved", out_path)
