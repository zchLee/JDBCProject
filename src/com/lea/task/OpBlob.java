package com.lea.task;

import com.lea.preparedstatement3.util.JDBCUtil;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 操作Blob数据
 *
 * @author lzc
 * @create 2020/09/20 下午 2:35
 */
public class OpBlob {

    @Test
    public void test() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        String sql = "insert into customers(name, email, birth, photo) value (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, "大东西");
        ps.setObject(2, "ddd@qq.com");
        ps.setObject(3, "2020-09-20");
        // 文件大于mysql默认配置文件大小时，需要去mysql.ini 配置文件修改Blob文件大小
//        max_allowed_packet=16M
        ps.setBlob(4, new FileInputStream("C:\\Users\\hasee\\Pictures\\壁纸\\33a3c5f6588860f4dc778d247745d44d.jpg"));
        ps.execute();
        JDBCUtil.close(conn, ps);
    }

    @Test
    public void test1() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        String sql = "select name, email, birth, photo from customers where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, 19);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");

            Blob photo = rs.getBlob("photo");
            InputStream is = photo.getBinaryStream();
            FileOutputStream fos = new FileOutputStream("dowload.jpg");
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = is.read(data)) != -1) {
                fos.write(data, 0, len);
            }
            is.close();
            fos.close();
        }
        JDBCUtil.close(conn, ps, rs);
    }
}
