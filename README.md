
# modvpn

modvpn (pronounced Up VPN) app is WireGuard VPN client for Linux, macOS, Windows, and Android.
For more information please visit https://modvpn.app

modvpn desktop app is made up of UI, CLI and background Daemon.

# Serverless

modvpn uses a Serverless computing model, where a Linux-based WireGuard server is provisioned on public cloud providers when app requests to connect to VPN. And server is deprovisioned when app requests to disconnect from VPN.

All of it happens with a single click or tap on the UI, or a single command on terminal.

# Install
App for Linux, macOS, Windows, and Android is available for download on [Github Releases](https://github.com/modvpn/modvpn-app/releases) or on website at https://modvpn.app/download


# Code

## Organization

| Crate or Directory | Description |
| --- | --- |
| modvpn-android | Standalone app for Android. |
| modvpn-cli | Code for `modvpn` cli. |
| modvpn-config | Configuration read from env vars, `modvpn.conf.toml` are merged at runtime in `modvpn-config` and is source of runtime configuration for `modvpn-cli`, `modvpn-daemon`, and `modvpn-ui`. |
| modvpn-controller | Defines GRPC protobuf for APIs exposed by `modvpn-daemon` to be consumed by `modvpn-cli` and `modvpn-ui`. |
| modvpn-daemon | Daemon is responsible for orchestrating a VPN session. It takes input from modvpn-cli or modvpn-ui via GRPC (defined in `modvpn-controller`) and make calls to backend server via separate GRPC (defined in `modvpn-server`). When backend informs that a server is ready daemon configures network tunnel, see [NetworkDependency.md](./NetworkDependency.md) for more info. |
| modvpn-entity | Defines data models used by modvpn-daemon to persist data on disk in sqlite database. |
| modvpn-migration | Defines database migration from which `modvpn-entity` is generated. |
| modvpn-packages| Contains resources to package binaries for distribution on macOS (pkg), Linux (rpm & deb), and Windows (msi). |
|modvpn-server| Contains GRPC protobuf definitions and code for communication with backend server. |
| modvpn-types | Defines common Rust types for data types used in various crates. These are also used to generate Typescript types for modvpn-ui for seamless serialization and deserialization across language boundaries. |
|modvpn-ui| A Tauri based desktop app. GPRC communication with daemon is done in Rust. Typescript code interact with Rust code via Tauri commands. |


## Building Desktop Apps

Please see [Build.md](./Build.md)

## Building Android App

Please see [modvpn-android/README.md](./modvpn-android/README.md)

# License

 Android app, and all Rust crates in this repository are [licensed under GPL version 3](./LICENSE).

Copyright (C) 2023  modvpn registered under HydroMind Technologies 2024

