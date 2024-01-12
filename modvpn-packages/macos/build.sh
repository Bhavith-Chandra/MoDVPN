#!/usr/bin/env bash

set -euo pipefail

VERSION=${1:-latest}

mkdir -p ./packages

declare -a BINARIES=(
    "./pkg/root/Applications/modvpn.app/Contents/Resources/modvpn"
    "./pkg/root/Applications/modvpn.app/Contents/Resources/modvpn-daemon"
    "./pkg/root/Applications/modvpn.app/Contents/MacOS/modvpn-ui"
)

sd "APP_VERSION" "${VERSION}" ./pkg/root/Applications/modvpn.app/Contents/Info.plist
sd "APP_VERSION" "${VERSION}" ./pkg/Distribution


if [[ ! -z "${APPLICATION_SIGNING_IDENTITY:-}" ]] && [[ ! -z "${APPLE_TEAM_ID:-}" ]]; then
    for binary in "${BINARIES[@]}"
    do
        echo "Signing: ${binary}"
        codesign \
            --options runtime \
            --sign "${APPLICATION_SIGNING_IDENTITY}" \
            "${binary}"
    done
fi

pkgbuild  \
    --install-location /Applications \
    --identifier app.modvpn.macos \
    --version "${VERSION}" \
    --scripts "./pkg/scripts" \
    --root "./pkg/root/Applications" \
    ./packages/app.modvpn.macos.pkg

productbuild \
    --distribution "./pkg/Distribution" \
    --resources "./pkg/Resources" \
    --package-path ./packages \
    "modvpn-${VERSION}-unsigned.pkg"

if [ ! -z "${INSTALLER_SIGNING_IDENTITY:-}" ]; then
    echo "Signing pkg"
    productsign \
        --sign "${INSTALLER_SIGNING_IDENTITY}" \
        "modvpn-${VERSION}-unsigned.pkg" "modvpn-${VERSION}.pkg"
else
    mv "modvpn-${VERSION}-unsigned.pkg" "modvpn-${VERSION}.pkg"
fi
