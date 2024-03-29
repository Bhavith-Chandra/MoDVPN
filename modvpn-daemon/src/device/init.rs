use modvpn_migration::sea_orm::DatabaseConnection;
use modvpn_types::device::DeviceDetails;

use super::storage::DeviceStorage;

pub async fn initialize_device(db: DatabaseConnection) -> Result<DeviceDetails, String> {
    let device_storage = DeviceStorage::new(db);
    device_storage.init().await
}
