package model

import model.Game.{Card, Point, lose, win}
import strategy.Strategy

import scala.collection.breakOut
import scala.util.Random

case class Game(players: Seq[Player], restPoint: Seq[Point]) {
  def one()(implicit random: Random): Game = {
    val (point +: nextPoints) = restPoint
    val cards: Map[Player, Card] = players.map { p => p -> p.putCard(this, point) }(breakOut)
    val target = if(point < 0) lose(cards) else win(cards)
    val afterPlayers = players.map { p =>
      val haveCards = p.haveCards - cards(p)
      val points = if(target.contains(p)) p.points :+ point else p.points
      p.copy(haveCards = haveCards, points = points)
    }
    Game(players = afterPlayers, nextPoints)
  }

  def result: String = {
    players.map { p => s"${p.name}: ${p.points.sum}" }.mkString("\n")
  }
}

object Game {
  type Card = Int
  type Point = Int
  val MinPoint: Point = -5
  val MaxPoint: Point = 10
  val InitialPoints: Seq[Int] = (1 to MaxPoint) ++ (MinPoint to -1)
  val MaxCard: Card = 15
  val MinCard: Card = 1
  val Cards: Set[Card] = (MinCard to MaxCard).toSet

  def play(game: Game)(implicit random: Random): List[Game] = {
    if(game.restPoint.isEmpty) Nil
    else {
      val next = game.one()
      next :: play(next)
    }
  }

  private def win[T](cards: Map[T, Card]): Option[T] = {
    if(cards.isEmpty) return None
    val max = cards.maxBy(_._2)._2
    if(cards.valuesIterator.count(_ == max) > 1) win(cards.filterNot(_._2 == max))
    else cards.find(_._2 == max).map(_._1)
  }

  private def lose[T](cards: Map[T, Card]): Option[T] = {
    if(cards.isEmpty) return None
    val min = cards.minBy(_._2)._2
    if(cards.valuesIterator.count(_ == min) > 1) lose(cards.filterNot(_._2 == min))
    else cards.find(_._2 == min).map(_._1)
  }
}

case class Player(
    name: String,
    private val strategy: Strategy,
    haveCards: Set[Game.Card] = Game.Cards,
    points: Seq[Game.Point] = Nil) {
  def putCard(game: Game, point: Point): Card = strategy.putCard(game, this, point)
}
