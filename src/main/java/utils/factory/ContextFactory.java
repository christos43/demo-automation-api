package utils.factory;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContextFactory {

  String authToken;

}
