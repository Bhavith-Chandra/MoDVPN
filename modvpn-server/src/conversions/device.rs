impl From<modvpn_types::modvpn_server::UserCredentials> for crate::proto::UserCredentials {
    fn from(value: modvpn_types::modvpn_server::UserCredentials) -> Self {
        Self {
            email: value.email,
            password: value.password,
        }
    }
}

impl From<modvpn_types::modvpn_server::DeviceInfo> for crate::proto::DeviceInfo {
    fn from(value: modvpn_types::modvpn_server::DeviceInfo) -> Self {
        Self {
            name: value.name,
            version: value.version,
            arch: value.arch,
            public_key: value.public_key,
            unique_id: value.unique_id.to_string(),
            device_type: value.device_type.into(),
        }
    }
}

impl From<crate::proto::DeviceAddresses> for modvpn_types::modvpn_server::DeviceAddresses {
    fn from(value: crate::proto::DeviceAddresses) -> Self {
        Self {
            ipv4_address: value.ipv4_address.into(),
        }
    }
}

impl From<modvpn_types::modvpn_server::AddDeviceRequest> for crate::proto::AddDeviceRequest {
    fn from(value: modvpn_types::modvpn_server::AddDeviceRequest) -> Self {
        Self {
            user_creds: Some(value.user_creds.into()),
            device_info: Some(value.device_info.into()),
        }
    }
}

impl From<crate::proto::AddDeviceResponse> for modvpn_types::modvpn_server::AddDeviceResponse {
    fn from(value: crate::proto::AddDeviceResponse) -> Self {
        Self {
            token: value.token,
            device_addresses: value.device_addresses.unwrap().into(),
        }
    }
}
