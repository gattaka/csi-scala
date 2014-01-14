package org.myftp.gattserver.csi.world.factories

import org.slf4j.LoggerFactory
import scala.util.Random
import org.myftp.gattserver.csi.world.World
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.world.PersonBuilder

object WorldFactory {

  val MAX_BASE_POPULATION = 10;
  val MIN_BASE_POPULATION = 5;
  val MAX_BASE_AGE = 60;
  val MIN_BASE_AGE = 18;

  val logger = LoggerFactory.getLogger(WorldFactory.getClass());

  def createWorld(): World = {

    val world = new World;

    // I. vygeneruj základ populace
    val basePopulation = Random.nextInt(MAX_BASE_POPULATION - MIN_BASE_POPULATION) + MIN_BASE_POPULATION;

    for (i <- 0 to basePopulation) {
      val person = PersonFactory.generatePerson(MAX_BASE_AGE, MIN_BASE_AGE, 0);
      logger.info("New character: " + person.toString());
      world.registerPerson(person);
    }

    // II. vztahy
    world.persons.foreach(RelationFactory.generateRelations(_, world));

    // TODO - po všech generacích teprve vygenerovat nìjaké nemorální vztahy
    // apod.

    // TODO - nazávìr vygenerovat tìžké vztahy a vraždu
    return world;
  }

}