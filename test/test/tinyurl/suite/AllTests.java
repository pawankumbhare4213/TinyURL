package test.tinyurl.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.tinyurl.service.TinyURLServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	TinyURLServiceTest.class
})
public class AllTests {

}
