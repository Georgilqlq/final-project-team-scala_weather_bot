package table

object Table:
  def plotTable(table: Seq[Seq[Any]]) = println(format(table))

  def format(table: Seq[Seq[Any]]) = table match
    case Seq() => ""
    case _ =>
      val sizes = for (row <- table) yield (for (cell <- row) yield if cell == null then 0 else cell.toString.length)
      val colSizes = for (col <- sizes.transpose) yield col.max
      val rows = for (row <- table) yield createRow(row, colSizes)
      constructRows(rows, separate(colSizes))

  def constructRows(rows: Seq[String], rowSeparator: String): String = (
    rowSeparator ::
      rows.head ::
      rowSeparator ::
      rows.tail.toList :::
      rowSeparator ::
      List()
  ).mkString("\n")

  def createRow(row: Seq[Any], colSizes: Seq[Int]) =
    val cells = for ((item, size) <- row.zip(colSizes)) yield if size == 0 then "" else ("%" + size + "s").format(item)
    cells.mkString("|", "|", "|")

  def separate(colSizes: Seq[Int]) = colSizes map { "-" * _ } mkString ("+", "+", "+")
