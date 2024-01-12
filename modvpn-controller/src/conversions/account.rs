impl From<crate::proto::SignInRequest> for modvpn_types::modvpn_server::UserCredentials {
    fn from(value: crate::proto::SignInRequest) -> Self {
        Self {
            email: value.email,
            password: value.password,
        }
    }
}
