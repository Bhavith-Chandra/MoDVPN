#!/usr/bin/env bash

set -ueo pipefail

LOG_DIR="/var/log/modvpn"
modvpn_DAEMON_PLIST_PATH="/Library/LaunchDaemons/app.modvpn.daemon.plist"

ask_confirmation() {
    read -p "Are you sure you want to stop and uninstall modvpn? (y/n) "
    if [[ "$REPLY" =~ [Yy]$ ]]; then
        echo "Uninstalling modvpn ..."
    else
        echo "Thank you for keeping modvpn"
        exit 0
    fi
}

stop_daemon_and_ui() {
    echo "Stopping and unloading modvpn-daemon ..."
    if [[ -f "${modvpn_DAEMON_PLIST_PATH}" ]]; then
        # Stop Daemon and UI
        sudo launchctl unload -w "${modvpn_DAEMON_PLIST_PATH}" || true
        sudo pkill -x "modvpn-ui" || true
        sudo rm "${modvpn_DAEMON_PLIST_PATH}" || true
    fi
}

uninstall() {
    echo "Removing files ..."
    sudo rm /usr/local/bin/modvpn || true
    sudo rm -rf /Applications/modvpn.app || true
    sudo pkgutil --forget app.modvpn.macos || true
    sudo rm -rf /var/log/modvpn || true
    sudo rm -rf /etc/modvpn || true
}

main() {
    ask_confirmation
    stop_daemon_and_ui
    uninstall
    echo "Done."
}

main "$@"
