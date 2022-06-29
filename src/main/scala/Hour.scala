case class Hour(
                 time_epoch: Int,
                 time: String,
                 temp_c: Double,
                 temp_f: Double,
                 is_day: Int,
                 condition: Condition,
                 wind_mph: Double,
                 wind_kph: Double,
                 wind_degree: Int,
                 wind_dir: String,
                 pressure_mb: Double,
                 pressure_in: Double,
                 precip_mm: Double,
                 precip_in: Double,
                 humidity: Int,
                 cloud: Int,
                 feelslike_c: Double,
                 feelslike_f: Double,
                 windchill_c: Double,
                 windchill_f: Double,
                 heatindex_c: Double,
                 heatindex_f: Double,
                 dewpoint_c: Double,
                 dewpoint_f: Double,
                 will_it_rain: Int,
                 chance_of_rain: Int,
                 will_it_snow: Int,
                 chance_of_snow: Int,
                 vis_km: Double,
                 vis_miles: Double,
                 gust_mph: Double,
                 gust_kph: Double,
                 uv: Double
               ):
  def myArgs: List[(String, Object)] =
    this.condition.myArgs ++ this.productElementNames.toList
      .filter(!_.equals("condition"))
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))