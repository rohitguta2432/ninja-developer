package io.rammila.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.rammila.api.contant.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "profiles")
@EntityListeners({AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull(message = Constants.REQUIRED)
    private String type;
    @NotNull(message = Constants.REQUIRED)
    private String title;
    private String img;
    @NotNull(message = Constants.REQUIRED)
    private String description;
    @NotNull(message = Constants.REQUIRED)
    private String perHourCharge;

    @Getter(onMethod_ = {@ApiModelProperty(hidden = true)})
    @CreatedDate
    private Date createdAt;

    @Getter(onMethod_ = {@ApiModelProperty(hidden = true)})
    @LastModifiedDate
    private Date updatedAt;


}
