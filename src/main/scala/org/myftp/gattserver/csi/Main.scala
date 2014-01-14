package org.myftp.gattserver.csi

import org.myftp.gattserver.csi.world.PersonBuilder
import org.myftp.gattserver.csi.world.factories.AddressFactory
import org.myftp.gattserver.csi.world.World
import org.myftp.gattserver.csi.world.factories.WorldFactory
import java.text.DecimalFormat
import org.slf4j.LoggerFactory

object Main {

  val logger = LoggerFactory.getLogger(Main.getClass());

  def main(args: Array[String]) {

    val world = WorldFactory.createWorld();

    val knowledge = world.knowledge;

    val numberFormat = new DecimalFormat("###.##");
    logger.info("World population: " + world.population);
    logger.info("World AVG age: " + numberFormat.format(world.averageAge));
    logger.info("World MAX age: " + numberFormat.format(world.maximumAge));
    logger.info("World MIN age: " + numberFormat.format(world.minimumAge));
    logger.info("World males: " + world.numberOfMales);
    logger.info("World females: " + world.numberOfFemales);
    logger.info("World M/F ratio: "
      + numberFormat.format(world.numberOfMales * 1.0 / world.numberOfFemales));

    for (relationType <- world.relationTypes) {
      logger.info("Number of relations of type '" + relationType.name + "': "
        + world.personsByRelations(relationType).size);
    }

  }

}