package utils.factory;


import dto.Booking;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContextFactory {

  String authToken;
  Booking booking;

}
