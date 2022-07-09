package enums

case class Location(
  name: String,
  region: String,
  country: String,
  lat: Double,
  lon: Double,
  tz_id: String,
  localtime_epoch: Int,
  localtime: String
):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Current(
  last_updated_epoch: Int,
  last_updated: String,
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
  vis_km: Double,
  vis_miles: Double,
  uv: Double,
  gust_mph: Double,
  gust_kph: Double
):
  def myArgs: List[(String, Object)] =
    this.condition.myArgs ++ this.productElementNames.toList
      .filter(!_.equals("condition"))
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Condition(text: String, icon: String, code: Int):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class CurrentResult(location: Location, current: Current):
  def myArgs: List[(String, Object)] = this.current.myArgs ++ this.location.myArgs
