package se.citerus.dddsample.client.location;

import org.apache.commons.lang.Validate;

import java.util.regex.Pattern;

/**
 * United nations location code.
 * <p/>
 * http://www.unece.org/cefact/locode/
 * http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE
 */
public final class UnLocode implements se.citerus.dddsample.domain.shared.ValueObject<UnLocode> { //TODO ValueObject is needed as dependency

    private String unlocode;

    // Country code is exactly two letters.
    // Location code is usually three letters, but may contain the numbers 2-9 as well
    private static final Pattern VALID_PATTERN = Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");

    /**
     * Constructor.
     *
     * @param countryAndLocation Location string.
     */
    public UnLocode(final String countryAndLocation) {
        Validate.notNull(countryAndLocation, "Country and location may not be null");
        Validate.isTrue(VALID_PATTERN.matcher(countryAndLocation).matches(),
                countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");

        this.unlocode = countryAndLocation.toUpperCase();
    }

    /**
     * @return country code and location code concatenated, always upper case.
     */
    public String idString() {
        return unlocode;
    }

    @Override
    public int hashCode() {
        return unlocode.hashCode();
    }

    @Override
    public boolean sameValueAs(UnLocode other) {
        return other != null && this.unlocode.equals(other.unlocode);
    }

    @Override
    public String toString() {
        return idString();
    }

    UnLocode() {
        // Needed by Hibernate
    }

    // [JTA] Added these methods as we require them for Hibernate in our client
    public String getUnlocode() {
        return unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }
}
