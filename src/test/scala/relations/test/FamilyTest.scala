package relations.test

import org.junit.Test
import org.myftp.gattserver.csi.world.PersonBuilder
import org.myftp.gattserver.csi.world.factories.AddressFactory
import org.myftp.gattserver.csi.world.factories.PersonFactory
import org.myftp.gattserver.csi.relations.ChildRelationType

class FamilyTest {

  @Test
  def test {

    // Mother
    val age = ChildRelationType.MIN_AGE_DIFF_RANGE;
    var personBuilder = PersonFactory.preparePersonBuilder(age, age, 0, false);
    val mother = personBuilder.createPerson
    
    // father 
    personBuilder = PersonFactory.preparePersonBuilder(age, age, 0, true);
    val father = personBuilder.createPerson
    
    // uncle
    
    // aunt
    
    // OK
    println("Test OK")

  }

}