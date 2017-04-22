package cn.qqtheme.framework.beans;

import java.util.List;

/**
 * Created by 李鹏 on 2017/04/21 0021.
 */

public class Province {
    private int id;
    private String name;
    private List<City> citys;


    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", citys=" + citys.toString() +
                '}';
    }

    public Province() {
    }

    public  Province(int id, String name, List<City> citys) {
        this.id = id;
        this.name = name;
        this.citys = citys;
    }

    public static class City{
        private int id;
        private String name;
        private List<District> districts;

        public City() {
        }

        public City(int id, String name, List<District> districts) {
            this.id = id;
            this.name = name;
            this.districts = districts;
        }

        @Override
        public String toString() {
            return "City{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", districts=" + districts.toString() +
                    '}';
        }

        public static class District{
            private int id;
            private String name;

            public District() {
            }

            public District(int id, String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public String toString() {
                return "District{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        '}';
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<District> getDistricts() {
            return districts;
        }

        public void setDistricts(List<District> districts) {
            this.districts = districts;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCitys() {
        return citys;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }
}
