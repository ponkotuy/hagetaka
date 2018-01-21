import java.util.concurrent.ThreadLocalRandom

import model.{Game, Player}
import strategy.RandomStrategy

import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    implicit val random: Random = new Random(ThreadLocalRandom.current())
    val players = (1 to 4).map(i => Player(s"player${i}", new RandomStrategy(random)))
    println(Game.play(Game(players, random.shuffle(Game.InitialPoints))).last.result)
  }
}
