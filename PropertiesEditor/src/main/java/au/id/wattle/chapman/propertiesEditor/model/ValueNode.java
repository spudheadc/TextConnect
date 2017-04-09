package au.id.wattle.chapman.propertiesEditor.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
@Data
@AllArgsConstructor
public class ValueNode {
	@NotNull
    @ApiModelProperty(notes = "The name of the file that includes this value.", required=true)
	private String filename;

	@NotNull
    @ApiModelProperty(notes = "The value that appears.", required=true)
	private String value;
}
