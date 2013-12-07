package org.myftp.gattserver.csi.world.factories

import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.palette.FirstName
import org.myftp.gattserver.csi.palette.SureName
import java.util.Date
import org.myftp.gattserver.csi.palette.Address

class PersonBuilder {

  var firstName: FirstName = null;
  var sureName: SureName = null;
  var maidenName: SureName = null;
  var birthDate: Date = null;
  var address: Address = null;
  var male: Boolean = true;

  def createPerson: Person = {
    if (firstName == null || sureName == null || (sureName == null && male == false) || birthDate == null || address == null)
      throw new IllegalStateException;

    return new Person(firstName, sureName, maidenName, birthDate, address, male);
  }

}