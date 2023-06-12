# ICrypt
Application to encrypt images using AES encryption algorithm - OFB mode, java language, packaged by Android Studio
# 
Người dùng sử dụng thuật toán mã hóa AES ở chế độ OFB để mã hóa hình ảnh có thể đọc hiểu chức năng khối qua sơ đồ hoạt động của ứng dụng.
Hình 2. 10 Hình sơ đồ hoạt động chung của AES - OFB image encryption
Cách thức hoạt động
1) Khởi chạy ứng dụng mã hóa hình ảnh AES-OFB và nhập mật khẩu.
2) Ứng dụng dùng bộ sinh Vector khởi tạo (ngẫu nhiên) sinh chuỗi ngẫu nhiên làm dữ liệu đầu vào, đưa vào hàm mã hóa, Thực thi giải thuật mã hóa cho khối trên với khóa mã K, đồng thời đầu ra hàm mã hóa trở thành IV tiếp theo để trở thành đầu vào của Khối mã hóa tiếp theo. 
3) Sử dụng IV Vector khởi tạo sinh ra cho cả chức năng mã hóa và giải mã dữ liệu.
Bên A: thực hiện chức năng mã hóa
4) Thực hiện chức năng mã hóa, nhập ảnh rõ 
5) Sử dụng IV Vector khởi tạo được sinh ra từ buớc 2 để Thực thi giải thuật mã hóa cho khối trên với khóa mã K, đồng thời đầu ra hàm mã hóa trở thành IV tiếp theo để trở thành đầu vào của Khối mã hóa tiếp theo. Đầu ra hàm mã hóa XOR với khối dữ liệu Rõ thu được C là ảnh mã.
6) Thu được file ảnh mã C = EAES-OFB (K, IV) xor Rõ. và lưu ảnh mã vào thư mục Download/mahoa trên Android.
Bên B: thực hiện chức năng giải mã
7) Nhập file ảnh mã C vào ứng dụng thực hiện chức năng giải mã
8) Sử dụng IV Vector khởi tạo được sinh ra từ buớc 2 để làm đầu vào hàm Giải mã, tiến hành giải mã, đầu ra hàm giải mã được XOR với khối dữ liệu Mã Kết quả thu được ảnh Rõ R. 
9) Thu được file ảnh rõ R = DAES-OFB (K, IV) xor Mã. và lưu file ảnh rõ vào thư mục Download/giaima trên Android.
