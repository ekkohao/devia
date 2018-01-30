/*
 * Copyright (c) 2018, jerehao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jerehao.devia.orm.model.support;

import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.orm.query.Property;

import java.util.Objects;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-24 20:06 jerehao
 */
public class Column extends ColumnDefinition {

    private static final Logger LOGGER = Logger.getLogger(Column.class);

    private Property property;

    public Column(String name, DataType dataType, Property property) {
        super(name, dataType);

        this.property = property;
    }

    //一些属性修正和类型匹配检查
    public void afterSetter() {
        checkGenerationPolicyAndResetToRightSetting();
    }

    private void checkGenerationPolicyAndResetToRightSetting() {
        if(getGenerationPolicy() == GenerationPolicy.UUID
                && (getType() != DataType.CHAR || getLength() != 32 || isAutoIncrement())) {
            setType(DataType.CHAR);
            setLength(32);
            setAutoIncrement(false);

            String msg = StringUtils.build("As pk column [{0}] generation policy is uuid type," +
                    " so reset DataType to Char, length to 32 and not auto_increment", getName());
            LOGGER.info(msg);
        }
        else if(getGenerationPolicy() == GenerationPolicy.AUTO && !isAutoIncrement()){
            setAutoIncrement(true);

            String msg = StringUtils.build("As pk column [{0}] generation policy is auto," +
                    " so reset auto_increment to true", getName());
            LOGGER.info(msg);
        }
    }

    public Property getProperty() {
        return property;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        return Objects.equals(column.getName(), getName())
                && Objects.equals(column.getType(), getType())
                && Objects.equals(column.getDefaultValue(), getDefaultValue())
                && Objects.equals(column.getLength(), getLength())
                && Objects.equals(column.getGenerationPolicy(), getGenerationPolicy())
                && Objects.equals(column.isPrimaryKey(), isPrimaryKey())
                && Objects.equals(column.isAutoIncrement(), isAutoIncrement())
                && Objects.equals(column.isNotnull(), isNotnull())
                && Objects.equals(column.isUnique(), isUnique());
    }
}
