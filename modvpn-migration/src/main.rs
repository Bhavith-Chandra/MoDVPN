use sea_orm_migration::prelude::*;

#[async_std::main]
async fn main() {
    cli::run_cli(modvpn_migration::Migrator).await;
}
