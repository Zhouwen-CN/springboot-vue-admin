package com.yeeiee.codegen.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yeeiee.domain.serde.EncryptSerializer;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 数据源配置表单对象
 * </p>
 *
 * @author chen
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@Schema(name = "DataSourceForm", description = "数据源配置表单")
public class DataSourceForm {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "主键")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "数据源名称")
    private String name;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 200)
    @Schema(description = "数据源连接")
    private String url;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "用户名")
    private String username;

    @JsonSerialize(using = EncryptSerializer.class)
    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "密码")
    private String password;

}
