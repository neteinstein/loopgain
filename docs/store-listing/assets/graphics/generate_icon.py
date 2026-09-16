import re
from PIL import Image, ImageDraw

VIEWPORT = 108.0
OUT = 512
SCALE = OUT / VIEWPORT
STROKE = 13.0 * SCALE
BG = (13, 34, 84, 255)  # #0D2254

def parse_path(d):
    nums = re.findall(r"-?\d+\.?\d*", d)
    coords = list(map(float, nums))
    pts = [(coords[i], coords[i + 1]) for i in range(0, len(coords), 2)]
    return pts

RIGHT_PATH = "M54.13,53.88 L54.66,53.35 L55.31,52.69 L55.98,52.04 L56.66,51.40 L57.35,50.77 L58.06,50.14 L58.79,49.53 L59.54,48.94 L60.33,48.36 L61.14,47.81 L61.99,47.30 L62.88,46.81 L63.81,46.38 L64.78,45.99 L65.79,45.67 L66.83,45.41 L67.92,45.24 L69.03,45.16 L70.17,45.19 L71.32,45.34 L72.47,45.62 L73.60,46.03 L74.68,46.59 L75.70,47.29 L76.63,48.14 L77.44,49.13 L78.10,50.23 L78.59,51.43 L78.90,52.70 L79.00,54.00 L78.90,55.30 L78.59,56.57 L78.10,57.77 L77.44,58.87 L76.63,59.86 L75.70,60.71 L74.68,61.41 L73.60,61.97 L72.47,62.38 L71.32,62.66 L70.17,62.81 L69.03,62.84 L67.92,62.76 L66.83,62.59 L65.79,62.33 L64.78,62.01 L63.81,61.62 L62.88,61.19 L61.99,60.70 L61.14,60.19 L60.33,59.64 L59.54,59.06 L58.79,58.47 L58.06,57.86 L57.35,57.23 L56.66,56.60 L55.98,55.96 L55.31,55.31 L54.66,54.65 L54.13,54.12"
RIGHT_GRAD = dict(sx=58, sy=47, ex=76, ey=61, sc=(0x5B, 0xB8, 0xE8), ec=(0x16, 0x3A, 0x66))

LEFT_PATH = "M53.87,53.88 L53.34,53.35 L52.69,52.69 L52.02,52.04 L51.34,51.40 L50.65,50.77 L49.94,50.14 L49.21,49.53 L48.46,48.94 L47.67,48.36 L46.86,47.81 L46.01,47.30 L45.12,46.81 L44.19,46.38 L43.22,45.99 L42.21,45.67 L41.17,45.41 L40.08,45.24 L38.97,45.16 L37.83,45.19 L36.68,45.34 L35.53,45.62 L34.40,46.03 L33.32,46.59 L32.30,47.29 L31.37,48.14 L30.56,49.13 L29.90,50.23 L29.41,51.43 L29.10,52.70 L29.00,54.00 L29.10,55.30 L29.41,56.57 L29.90,57.77 L30.56,58.87 L31.37,59.86 L32.30,60.71 L33.32,61.41 L34.40,61.97 L35.53,62.38 L36.68,62.66 L37.83,62.81 L38.97,62.84 L40.08,62.76 L41.17,62.59 L42.21,62.33 L43.22,62.01 L44.19,61.62 L45.12,61.19 L46.01,60.70 L46.86,60.19 L47.67,59.64 L48.46,59.06 L49.21,58.47 L49.94,57.86 L50.65,57.23 L51.34,56.60 L52.02,55.96 L52.69,55.31 L53.34,54.65 L53.87,54.12"
LEFT_GRAD = dict(sx=32, sy=47, ex=50, ey=61, sc=(0xFF, 0xFF, 0xFF), ec=(0xC8, 0xDF, 0xEF))


def lerp(a, b, t):
    return tuple(round(a[i] + (b[i] - a[i]) * t) for i in range(3))


def grad_color(x, y, g):
    dx, dy = g["ex"] - g["sx"], g["ey"] - g["sy"]
    denom = dx * dx + dy * dy
    t = ((x - g["sx"]) * dx + (y - g["sy"]) * dy) / denom
    t = max(0.0, min(1.0, t))
    return lerp(g["sc"], g["ec"], t)


def draw_path(draw, path_d, grad):
    pts = parse_path(path_d)
    for i in range(len(pts) - 1):
        x0, y0 = pts[i]
        x1, y1 = pts[i + 1]
        c0 = grad_color(x0, y0, grad)
        c1 = grad_color(x1, y1, grad)
        c = tuple((c0[k] + c1[k]) // 2 for k in range(3))
        draw.line(
            [(x0 * SCALE, y0 * SCALE), (x1 * SCALE, y1 * SCALE)],
            fill=c + (255,),
            width=round(STROKE),
            joint="curve",
        )
        r = STROKE / 2
        draw.ellipse(
            [x0 * SCALE - r, y0 * SCALE - r, x0 * SCALE + r, y0 * SCALE + r],
            fill=c0 + (255,),
        )
    # cap the last point too
    xl, yl = pts[-1]
    cl = grad_color(xl, yl, grad)
    r = STROKE / 2
    draw.ellipse(
        [xl * SCALE - r, yl * SCALE - r, xl * SCALE + r, yl * SCALE + r],
        fill=cl + (255,),
    )


img = Image.new("RGBA", (OUT, OUT), BG)
draw = ImageDraw.Draw(img)
draw_path(draw, RIGHT_PATH, RIGHT_GRAD)
draw_path(draw, LEFT_PATH, LEFT_GRAD)

import os
out_path = os.path.join(os.path.dirname(__file__), "icon_512.png")
img.save(out_path)
print("saved", out_path)
