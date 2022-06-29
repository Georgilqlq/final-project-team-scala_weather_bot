case class Astro(
                  sunrise: String,
                  sunset: String,
                  moonrise: String,
                  moonset: String,
                  moon_phase: String,
                  moon_illumination: String
                ):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Astronomy(astro: Astro):
  def myArgs: List[(String, Object)] = this.astro.myArgs

case class AstronomyResult(location: Location, astronomy: Astronomy):
  def myArgs: List[(String, Object)] = this.location.myArgs ++ this.astronomy.myArgs