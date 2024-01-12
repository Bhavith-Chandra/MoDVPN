impl From<crate::proto::Location> for modvpn_types::location::Location {
    fn from(value: crate::proto::Location) -> Self {
        Self {
            code: value.code,
            country: value.country,
            country_code: value.country_code,
            city: value.city,
            city_code: value.city_code,
            state: value.state,
            state_code: value.state_code,
            estimate: value.estimate,
        }
    }
}

impl From<crate::proto::ListLocationsResponse> for Vec<modvpn_types::location::Location> {
    fn from(value: crate::proto::ListLocationsResponse) -> Self {
        value
            .locations
            .into_iter()
            .map(modvpn_types::location::Location::from)
            .collect()
    }
}
