
package com.zoomkey.core;

import com.zoomkey.framework.util.LogUtil;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class CustomDriverManagerDataSource extends DriverManagerDataSource {

    @Override
    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        final Properties mergedProps = new Properties();
        final Properties connProps = getConnectionProperties();
        if (connProps != null) {
            mergedProps.putAll(connProps);
        }
        if (username != null) {
            mergedProps.setProperty("user", username);
        }
        if (password != null) {
            // 此处开始根据自定义解密算法获取对应password
            String key;
            try {
                key = DESCoder.initKey(DESCoder.DES_KEY);
                final byte[] decPassword = DESCoder.decryptBASE64(password);
                final byte[] outputData = DESCoder.decrypt(decPassword, key);
                password = new String(outputData);
            } catch (final Exception e) {
                this.logger.error(LogUtil.stackTraceToString(e));
            }
            mergedProps.setProperty("password", password);
        }
        return getConnectionFromDriver(mergedProps);
    }
}
