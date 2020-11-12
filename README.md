# 自用的数据库
自用的数据库连接池,使用简单,代码小，只有40kb左右


        DBConfig config = new DBConfig();
        // 设置数据库参数 
        config.setDbDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        config.setDbHost("127.0.0.1");
        config.setDbPort(1433);
        config.setDbName("test");
        config.setUser("sa");
        config.setPassword("123456");

        // 创建连接工厂
        DefaultPooledFactory pooledFactory = new DefaultPooledFactory(config);
		// 实例化数据库连接池  这里可以根据业务自己去扩展
        YxbbDataSource dbPoolUtil = new YxbbDataSource(pooledFactory);
		
		 DBConnection dbconn = null;
		 try {
             dbconn = dbPoolUtil.borrowObject();
            Connection conn = dbconn.getConnection();
            PreparedStatement statement = conn.prepareStatement("select top 1 * from vip_AddFee where JBR=?");
            statement.setString(1, "唐燕");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                 System.out.println(rs.getDouble(field));
               }
           } catch (Exception e) {
                        e.getStackTrace();
            } finally {
                        dbPoolUtil.returnObject(dbUtil);
            }