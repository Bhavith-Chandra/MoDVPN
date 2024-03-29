pub async fn remove_old_socket_file() {
    if let Err(e) = tokio::fs::remove_file(modvpn_config::config().socket_path()).await {
        if e.kind() != std::io::ErrorKind::NotFound {
            tracing::error!(
                "Failed to remove old socket file {}",
                modvpn_config::config().socket_path().display()
            );
        }
    }
}
