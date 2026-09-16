# Store graphics — how these were generated

Both files here are generated, not hand-drawn, from the app's own brand source:

- `generate_icon.py` → `icon_512.png` (512×512 hi-res icon for Play Console). Rasterizes the
  exact path data and linear gradients from
  `androidApp/src/main/res/drawable/ic_launcher_foreground.xml` and
  `ic_launcher_background.xml` at full resolution, rather than upscaling the small
  (max 192×192) launcher PNGs in `androidApp/src/main/res/mipmap-*`.
- `generate_feature_graphic.py` → `feature_graphic_1024x500.png` (Play Console feature
  graphic). Same mark, plus the `LoopGainWordmark` red and the loading-screen's blue gradient
  and tagline (`LoadingScreen.kt`), laid out as a static banner.

Re-run either script (`python3 generate_icon.py`) after the launcher icon or brand colors
change, from inside this directory — each writes its PNG next to itself. Both need Pillow
(`pip install pillow`) and, for the feature graphic, macOS's bundled Arial fonts
(`/System/Library/Fonts/Supplemental/Arial*.ttf`); swap the font paths if generating on a
different OS.
