impl From<modvpn_types::location::Location> for crate::proto::Location {
    fn from(value: modvpn_types::location::Location) -> Self {
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

impl From<Vec<modvpn_types::location::Location>> for crate::proto::Locations {
    fn from(value: Vec<modvpn_types::location::Location>) -> Self {
        Self {
            location: value
                .into_iter()
                .map(crate::proto::Location::from)
                .collect(),
        }
    }
}

impl From<crate::proto::Locations> for Vec<modvpn_types::location::Location> {
    fn from(value: crate::proto::Locations) -> Self {
        value
            .location
            .into_iter()
            .map(modvpn_types::location::Location::from)
            .collect()
    }
}
