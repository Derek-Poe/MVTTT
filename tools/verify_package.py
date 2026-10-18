"""Verify the built WAR contains the exact source assets and no servlet API."""
from pathlib import Path
from zipfile import ZipFile

root = Path(__file__).resolve().parents[1]
assets = {
    "welcome.html": "Server/src/main/pages/welcome.html",
    "web_mobile/index.html": "UI/web_mobile/src/index.html",
    "web_mobile/mvttt_web.css": "UI/web_mobile/src/mvttt_web.css",
    "web_mobile/mvttt_web.js": "UI/web_mobile/src/mvttt_web.js",
}
with ZipFile(root / "Server/target/MVTTT.war") as war:
    names = war.namelist()
    for packaged, source in assets.items():
        assert war.read(packaged) == (root / source).read_bytes(), packaged
    assert "WEB-INF/classes/com/unstoppapoenguyen/mvttt/DatabaseConfig.class" in names
    assert not any("servlet-api" in name for name in names)
    assert not any("archive/" in name or "MVTTT_Schema.sql" in name for name in names)
schema = (root / "Server/MVTTT_Schema.sql").read_text(encoding="utf-8")
assert "INSERT INTO" not in schema.upper(), "Schema must not contain exported records"
assert "DEFINER=" not in schema.upper(), "Schema must not embed an account definer"
assert "CREATE" in schema and "cleanupMatches" in schema
print("WAR assets, servlet dependency scope, and empty-schema checks passed.")
