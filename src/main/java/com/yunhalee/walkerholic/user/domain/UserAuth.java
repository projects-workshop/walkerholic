package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Getter
@NoArgsConstructor
public class UserAuth {

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Builder
    public UserAuth(@NonNull String email, @NonNull String password, ProviderType providerType) {
        this.email = email;
        this.password = password;
        this.providerType = providerType;
    }

    public void update(User toUser) {
        this.email = toUser.getEmail();
        updatePassword(toUser.getPassword());
    }

    private void updatePassword(String updatedPassword) {
        if (!updatedPassword.isBlank() || !updatedPassword.isEmpty()) {
            this.password = updatedPassword;
        }
    }

    public void changePassword(String changedPassword) {
        this.password = changedPassword;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuth userAuth = (UserAuth) o;
        return Objects.equals(email, userAuth.email) && Objects
            .equals(password, userAuth.password) && providerType == userAuth.providerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, providerType);
    }

}
