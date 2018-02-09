package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.common.protocols.server.Server;
import com.complexible.common.protocols.server.ServerException;
import com.complexible.stardog.Stardog;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.google.common.io.Files;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

@RunWith(Suite.class)
@SuiteClasses({
  	TestAbbreviate.class,
	TestAbbreviateMiddle.class,
	TestAbbreviateWithMarker.class,
	TestAppendIfMissing.class,
	TestAppendIfMissingIgnoreCase.class,
	TestCapitalize.class,
	TestCaseFormat.class,
	TestCenter.class,
	TestChomp.class,
	TestChop.class,
	TestCommonPrefix.class,
	TestCommonSuffix.class,
	TestCompare.class,
	TestCompareIgnoreCase.class,
	TestContains.class,
	TestContainsAny.class,
	TestContainsIgnoreCase.class,
	TestContainsNone.class,
	TestContainsOnly.class,
	TestContainsWhitespace.class,
	TestCountMatches.class,
	TestDefaultIfBlank.class,
	TestDefaultIfEmpty.class,
	TestDeleteWhitespace.class,
	TestDifference.class,
	TestDigits.class,
	TestEndsWith.class,
	TestEndsWithIgnoreCase.class,
	TestEquals.class,
	TestEqualsAny.class,
	TestEqualsIgnoreCase.class,
	TestIndexOf.class,
	TestIndexOfAnyChar.class,
	TestIndexOfAnyCharBut.class,
	TestIndexOfAnyString.class,
	TestIndexOfAnyStringBut.class,
	TestIndexOfDifference.class,
	TestIndexOfIgnoreCase.class,
	TestInitials.class,
	TestIsAllBlank.class,
	TestIsAllLowerCase.class,
	TestIsAllUpperCase.class,
	TestIsAlpha.class,
	TestIsAlphaSpace.class,
	TestIsAlphanumeric.class,
	TestIsAlphanumericSpace.class,
	TestIsAnyBlank.class,
	TestIsAnyEmpty.class,
	TestIsAsciiPrintable.class,
	TestIsBlank.class,
	TestIsEmpty.class,
	TestIsMixedCase.class,
	TestIsNoneBlank.class,
	TestIsNoneEmpty.class,
	TestIsNotBlank.class,
	TestIsNotEmpty.class,
	TestIsNumeric.class,
	TestIsNumericSpace.class,
	TestIsWhitespace.class,
	TestJoin.class,
	TestJoinWith.class,
	TestLastIndexOf.class,
	TestLastIndexOfAny.class,
	TestLastIndexOfIgnoreCase.class,
	TestLastOrdinalIndexOf.class,
	TestLeft.class,
	TestLeftPad.class,
	TestLength.class,
	TestLowerCase.class,
	TestLowerCaseFully.class,
	TestMid.class,
	TestNormalizeSpace.class,
	TestOrdinalIndexOf.class,
	TestOverlay.class,
	TestPadEnd.class,
	TestPadStart.class,
	TestPrependIfMissing.class,
	TestPrependIfMissingIgnoreCase.class,
	TestRandom.class,
	TestRemove.class,
	TestRemoveAll.class,
	TestRemoveEnd.class,
	TestRemoveEndIgnoreCase.class,
	TestRemoveFirst.class,
	TestRemoveIgnoreCase.class,
	TestRemovePattern.class,
	TestRemoveStart.class,
	TestRemoveStartIgnoreCase.class,
	TestRepeat.class,
	TestReplace.class,
	TestReplaceAll.class,
	TestReplaceChars.class,
	TestReplaceEach.class,
	TestReplaceEachRepeatedly.class,
	TestReplaceFirst.class,
	TestReplaceIgnoreCase.class,
	TestReplaceOnce.class,
	TestReplaceOnceIgnoreCase.class,
	TestReplacePattern.class,
	TestReverse.class,
	TestReverseDelimited.class,
	TestRight.class,
	TestRightPad.class,
	TestRotate.class,
	TestSplit.class,
	TestSplitByCharacterType.class,
	TestStartsWith.class,
	TestStartsWithAny.class,
	TestStartsWithIgnoreCase.class,
	TestStrip.class,
	TestStripAccents.class,
	TestStripAll.class,
	TestStripEnd.class,
	TestStripStart.class,
	TestSubstringAfterLast.class,
	TestSubstring.class,
	TestSubstringAfter.class,
	TestSubstringBefore.class,
	TestSubstringBeforeLast.class,
	TestSubstringBetween.class,
	TestSwapCase.class,
	TestTrim.class,
	TestTruncate.class,
	TestUncapitalize.class,
	TestUnwrap.class,
	TestUpperCase.class,
	TestUpperCaseFully.class,
	TestWrap.class,
	TestWrapIfMissing.class
})

public class StringTestSuite extends TestCase {

	private static Stardog STARDOG;
	private static Server SERVER;
	public static final String DB = "test";
	public static final int TEST_PORT = 5888;
	private static final String STARDOG_HOME = System.getenv("STARDOG_HOME");
	protected Connection connection;
	private static final String STARDOG_LICENCE_KEY_FILE_NAME = "stardog-license-key.bin";

    @BeforeClass
    public static void beforeClass() throws IOException, ServerException {

        final File TEST_HOME;

        TEST_HOME = Files.createTempDir();
        TEST_HOME.deleteOnExit();

        Files.copy(new File(STARDOG_HOME + "/" + STARDOG_LICENCE_KEY_FILE_NAME),
                new File(TEST_HOME, STARDOG_LICENCE_KEY_FILE_NAME));

        STARDOG = Stardog.builder().home(TEST_HOME).create();

        SERVER = STARDOG.newServer()
                //.set(ServerOptions.SECURITY_DISABLED, true)
                .bind(new InetSocketAddress("localhost", TEST_PORT))
                .start();

        final AdminConnection adminConnection = AdminConnectionConfiguration.toEmbeddedServer()
                .credentials("admin", "admin")
                .connect();

        if (adminConnection.list().contains(DB)) {
            adminConnection.drop(DB);
        }

        adminConnection.newDatabase(DB).create();
    }

    @AfterClass
    public static void afterClass() {
        if (SERVER != null) {
            SERVER.stop();
        }
        STARDOG.shutdown();
    }
}
