package scala.test

import org.myftp.gattserver.csi.world.factories.PersonBuilder
import org.myftp.gattserver.csi.palette.factories.AddressFactory

object HelloWorld {

  def main(args: Array[String]) {
    val builder = new PersonBuilder;
    builder.address = AddressFactory.getRandomAddress();
  }

}