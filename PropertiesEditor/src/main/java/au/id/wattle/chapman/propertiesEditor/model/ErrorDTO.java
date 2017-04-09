package au.id.wattle.chapman.propertiesEditor.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class ErrorDTO {

    @ApiModelProperty(notes = "An error code that identifies what error occured.")
	private long errorCode;
    @ApiModelProperty(notes = "An error message that identifies what error occured.")
	private String errorMessage;
    @ApiModelProperty(notes = "A list of other error attributes.")
	private List<String> errorAttributes;
}
