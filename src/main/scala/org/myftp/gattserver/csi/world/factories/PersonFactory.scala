package org.myftp.gattserver.csi.world.factories;

import java.util.Calendar
import org.myftp.gattserver.csi.palette.Address
import org.myftp.gattserver.csi.palette.FirstName
import org.myftp.gattserver.csi.palette.SureName
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.world.PersonBuilder
import scala.util.Random
import org.myftp.gattserver.csi.palette.FirstName
import java.util.Date

object PersonFactory {

  def generatePerson(age: Int, yearOffset: Int): Person = {
    return generatePerson(age, age, yearOffset);
  }

  def preparePersonBuilder(maxAge: Int, minAge: Int, yearOffset: Int, male: Boolean = Random.nextBoolean()): PersonBuilder = {
    val personBuilder = new PersonBuilder;

    personBuilder.address = AddressFactory.getRandomAddress();
    personBuilder.male = male;
    personBuilder.firstName = FirstNameFactory.getRandomName(personBuilder.male);
    personBuilder.sureName = SureNameFactory.getRandomName();
    personBuilder.birthDate = generateBirthDate(maxAge, minAge, yearOffset);

    return personBuilder;
  }

  def generatePerson(maxAge: Int, minAge: Int, yearOffset: Int): Person = {
    return preparePersonBuilder(maxAge, minAge, yearOffset).createPerson;
  }

  def generateBirthDate(age: Int): Date = generateBirthDate(age, 0)
  def generateBirthDate(age: Int, yearOffset: Int): Date = generateBirthDate(age, age, yearOffset)
  def generateBirthDate(maxAge: Int, minAge: Int, yearOffset: Int): Date = {
    val calendar = Calendar.getInstance();
    val ageRandom = if (maxAge == minAge) 0 else Random.nextInt(maxAge - minAge);
    val year = Calendar.getInstance().get(Calendar.YEAR) - ageRandom
    -minAge - yearOffset;
    calendar.set(year, Random.nextInt(12) + 1, Random.nextInt(28) + 1);
    return calendar.getTime();
  }

}
