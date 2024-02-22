package swiggy.wallet.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.valueObject.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("userResponse")
public class UserResponse {
    private Long id;
    private String username;
    private Wallet wallet;
    private String message;
    public UserResponse(String message){
        this.message = message;
    }
}
