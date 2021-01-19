# 自用的数据库连接池
自用的数据库连接池,使用简单,代码小，只有40kb左右


         YxbbDataSourceConfig config = new YxbbDataSourceConfig();
         config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         config.setJdbcUrl("jdbc:sqlserver://192.168.0.82:1433;DatabaseName=test");
         config.setUser("sa");
         config.setPassword("123456");

        // 创建连接工厂
        DefaultPooledFactory pooledFactory = new DefaultPooledFactory(config);
		// 实例化数据库连接池  这里可以根据业务自己去扩展
        YxbbDataSource dbPoolUtil = new YxbbDataSource(pooledFactory);
		
		 DBConnection conn = null;
		 try {
             conn = dbPoolUtil.borrowObject();           
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