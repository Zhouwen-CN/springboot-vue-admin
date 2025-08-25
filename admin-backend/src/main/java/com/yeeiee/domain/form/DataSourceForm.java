package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
public class DataSourceForm implements FormToBeanHelper<DataSource> {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "主键")
    private Long id;
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 1, max = 50)
    @Schema(description = "数据源名称")
    private String name;
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 1, max = 500)
    @Schema(description = "数据源连接")
    private String url;
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 1, max = 50)
    @Schema(description = "用户名")
    private String username;
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 1, max = 50)
    @Schema(description = "密码")
    private String password;

}
