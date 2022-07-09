package enums

case class Matches(
  stadium: String,
  country: String,
  region: String,
  tournament: String,
  start: String,
  `match`: String
):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))
