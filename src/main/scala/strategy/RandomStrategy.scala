package strategy

import model.{Game, Player}

import scala.util.Random

class RandomStrategy(random: Random) extends Strategy {
  override def putCard(game: Game, player: Player, point: Game.Point): Game.Card = {
    val cards = player.haveCards
    cards.toVector(random.nextInt(cards.size))
  }
}
