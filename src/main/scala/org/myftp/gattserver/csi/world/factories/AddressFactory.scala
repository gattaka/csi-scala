package org.myftp.gattserver.csi.world.factories

import scala.util.Random
import org.myftp.gattserver.csi.palette.Address

object AddressFactory {

  private val addresses = List("Baker street",
    "Causton alley", "Europe street", "Highlane", "Breeks",
    "Jackobstown", "Redtown");

  private val random = new Random();

  def getRandomAddress() : Address = {
    val randomIndex = random.nextInt(addresses.size);
    return Address(addresses(randomIndex));
  }

}