package app.modvpn.modvpn.data.db

fun Location.toModelLocation(): app.modvpn.modvpn.model.Location =
    app.modvpn.modvpn.model.Location(
        code = this.code,
        city = this.city,
        cityCode = this.cityCode,
        country = this.country,
        countryCode = this.countryCode,
        state = this.state,
        stateCode = this.stateCode
    )

fun app.modvpn.modvpn.model.Location.toDbLocation(): Location =
    Location(
        code = this.code,
        city = this.city,
        cityCode = this.cityCode,
        country = this.country,
        countryCode = this.countryCode,
        state = this.state,
        stateCode = this.stateCode
    )
