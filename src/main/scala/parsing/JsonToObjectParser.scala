package parsing

import parsing.*

class JsonToObjectParser:

  def jsonToCurrent(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      ("id", row(0)) +: new JsonParsedCurrent(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no current weather results!")
    else List(argsList.map(_._1), argsList.map(_._2))

  def jsonToAstronomy(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      ("id", row(0)) +: new JsonParsedAstronomy(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no astronomy results!")
    else List(argsList.map(_._1), argsList.map(_._2))

  def jsonToFootball(row: List[String]): List[List[Object]] =
    val argsList: List[List[(String, Object)]] =
      new JsonParsedFootball(row(2), Map[String, Seq[String]]()).parsedValue.map(("id", row(0)) +: _.myArgs)
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no matches!")
    else argsList(0).map(_._1) +: argsList.map(_.map(_._2))

  def jsonToForecast(row: List[String]): List[List[Object]] =
    val argsList: List[List[(String, Object)]] =
      new JsonParsedForecast(row(2), Map[String, Seq[String]]()).parsedValue.map(("id", row(0)) +: _.myArgs)
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no days in the forecast!")
    else
      println(argsList(0).map(_._1))
      argsList(0).map(_._1) +: argsList.map(_.map(_._2))

  def jsonToTimeZone(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      ("id", row(0)) +: new JsonParsedTimeZone(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no timezone results!")
    else List(argsList.map(_._1), argsList.map(_._2))
