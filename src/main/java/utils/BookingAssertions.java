//package utils;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class BookingAssertions {
//
//    private BookingAssertions() {
//        // private constructor to prevent instantiation
//    }
//
//    public static void assertBookingsEqual(Booking expected, Booking actual) {
//        assertThat(actual)
//                .usingRecursiveComparison()
//                .ignoringCollectionOrderInFields("additionalNeeds") // in case additionalNeeds is not important
//                .isEqualTo(expected);
//    }
//}
