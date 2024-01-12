fn main() -> Result<(), Box<dyn std::error::Error>> {
    tonic_build::configure()
        .build_server(false)
        .compile(&["proto/modvpn-server.proto"], &["proto"])?;
    println!("cargo:rerun-if-changed=proto");
    Ok(())
}
