package strategy

import model.{Game, Player}
import model.Game._

trait Strategy {
  def putCard(game: Game, player: Player, point: Point): Card
}
