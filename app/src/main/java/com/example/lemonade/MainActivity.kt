/*
 * Copyright (C) 2021 The Android Open Source Project.
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
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.nio.file.Files.find

class MainActivity : AppCompatActivity() {

    /**
     * KHÔNG ĐƯỢC THAY THẾ BẤT KỲ TÊN BẤT KỲ TÊN HOẶC GIÁ TRỊ NÀO HOẶC GIÁ TRỊ BAN ĐẦU CỦA CHÚNG.
     *
     * Bất kỳ thứ gì có nhãn var thay vì val sẽ được thay đổi trong các chức năng nhưng KHÔNG ĐƯỢC
     * thay đổi các giá trị ban đầu được khai báo ở đây, điều này có thể khiến ứng dụng không hoạt động bình thường.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"

    // SELECT represents the "pick lemon" state
    // SELECT đại diện cho trạng thái "pick lemon"
    private val SELECT = "select"

    // SQUEEZE represents the "squeeze lemon" state
    // SQUEEZE đại diện cho trạng thái "squeeze lemon"
    private val SQUEEZE = "squeeze"

    // DRINK represents the "drink lemonade" state
    // DRINK đại diện cho trạng thái "drink lemonade"
    private val DRINK = "drink"

    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    // RESTART đại diện cho trạng thái nước chanh đã được uống và ly cạn
    private val RESTART = "restart"

    // Default the state to select
    // Mặc định trạng thái để chọn
    private var lemonadeState = "select"

    // Default lemonSize to -1
    // Kích thước chanh mặc định
    private var lemonSize = -1

    // Default the squeezeCount to -1
    // Mặc định số tiền ép là -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null
    private var a = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSnackbar()
        val viewload : TextView = findViewById(R.id.textView)
        // === KHÔNG THAY MÃ Ở SAU NẾU CÓ BÁO CÁO ===
        if (savedInstanceState != null) {
            lemonadeState =
                savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === KẾT THÚC NẾU THỐNG KÊ ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            // TODO: call the method that handles the state when the image is clicked
            // translate: gọi phương thức xử lý trạng thái khi hình ảnh được nhấp vào
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            // TODO: replace 'false' with a call to the function that shows the squeeze count
            // translate: thay thế 'false' bằng một lệnh gọi đến hàm hiển thị số lần ép
            showSnackbar()
        }
    }

    /**
     * === KHÔNG THÊM PHƯƠNG PHÁP NÀY ===
     *
     * Phương pháp này lưu trạng thái của ứng dụng nếu nó được đặt ở chế độ nền.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Việc nhấp chuột sẽ tạo ra một phản hồi khác nhau tùy thuộc vào trạng thái.
     * Phương pháp này xác định trạng thái và tiến hành hành động chính xác.
     */
    private fun clickLemonImage() {
        // TODO: use a conditional statement like 'if' or 'when' to track the lemonadeState
        //  when the image is clicked we may need to change state to the next step in the
        //  lemonade making progression (or at least make some changes to the current state in the
        //  case of squeezing the lemon). That should be done in this conditional statement

        //DICH
        // VIEC CAN LAM: sử dụng câu lệnh có điều kiện như 'if' hoặc 'when' để theo dõi lemonadeState
        // khi hình ảnh được nhấp vào, chúng ta có thể cần thay đổi trạng thái sang bước tiếp theo trong
        // nước chanh đang tiến triển (hoặc ít nhất là thực hiện một số thay đổi đối với trạng thái hiện tại trong
        // trường hợp vắt chanh). Điều đó nên được thực hiện trong câu lệnh điều kiện này

        when (lemonadeState) {
            SELECT -> {
                lemonadeState = SQUEEZE
                val tree: LemonTree = lemonTree
                lemonSize = tree.pick()
                squeezeCount = 0
            }
            SQUEEZE -> {
                squeezeCount += 1
                lemonSize -= 1
                lemonadeState = if (lemonSize == 0) {
                    DRINK
                } else SQUEEZE
            }
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
            }

            RESTART -> lemonadeState = SELECT
        }
        setViewElements()


        // TODO: When the image is clicked in the SELECT state, the state should become SQUEEZE
        //  - The lemonSize variable needs to be set using the 'pick()' method in the LemonTree class
        //  - The squeezeCount should be 0 since we haven't squeezed any lemons just yet.

        // VIỆC CẦN LÀM: Khi hình ảnh được nhấp ở trạng thái CHỌN, trạng thái sẽ trở thành SQUEEZE
        // - Biến lemonSize cần được đặt bằng phương thức 'pick ()' trong lớp LemonTree
        // - Số tiền vắt phải bằng 0 vì chúng tôi chưa vắt quả chanh nào.


        // TODO: When the image is clicked in the SQUEEZE state the squeezeCount needs to be
        //  INCREASED by 1 and lemonSize needs to be DECREASED by 1.
        //  - If the lemonSize has reached 0, it has been juiced and the state should become DRINK
        //  - Additionally, lemonSize is no longer relevant and should be set to -1
        // VIỆC CẦN LÀM: Khi hình ảnh được nhấp ở trạng thái SQUEEZE, số tiền ép phải được
        // TĂNG 1 và lemonSize cần được GIẢM 1.
        // - Nếu LemonSize đã về 0, nó đã được ép và trạng thái sẽ trở thành DRINK
        // - Ngoài ra, LemonSize không còn phù hợp nữa và phải được đặt thành -1


        // TODO: When the image is clicked in the DRINK state the state should become RESTART
        // VIỆC CẦN LÀM: Khi hình ảnh được nhấp vào ở trạng thái DRINK, trạng thái sẽ trở thành RESTART

        // TODO: When the image is clicked in the RESTART state the state should become SELECT
        // VIỆC CẦN LÀM: Khi hình ảnh được nhấp ở trạng thái RESTART, trạng thái sẽ trở thành CHỌN

        // TODO: lastly, before the function terminates we need to set the view elements so that the
        //  UI can reflect the correct state
        // VIỆC CẦN LÀM: cuối cùng, trước khi hàm kết thúc, chúng ta cần thiết lập các phần tử khung nhìn để
        // Giao diện người dùng có thể phản ánh đúng trạng thái


    }

    /**
     * Thiết lập các phần tử xem theo trạng thái.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        val lemonImage: ImageView = findViewById(R.id.image_lemon_state)

        when (lemonadeState) {
            SELECT -> {
                textAction.text = "Click to select a lemon!"
                lemonImage.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.text = "Click to juice the lemon!"
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.text = "Click to drink your lemonade!"
                lemonImage.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
            textAction.text = "Click to start againt!"
            lemonImage.setImageResource(R.drawable.lemon_restart)
        }

        }


        // TODO: set up a conditional that tracks the lemonadeState
        // VIEC CAN LAM: thiết lập một điều kiện theo dõi lemonadeState

        // TODO: for each state, the textAction TextView should be set to the corresponding string from
        //  the string resources file. The strings are named to match the state
        // DICH
        // VIEC CAN LAM: đối với mỗi trạng thái, TextView textAction nên được đặt thành chuỗi tương ứng từ
        // tệp tài nguyên chuỗi. Các chuỗi được đặt tên để phù hợp với trạng thái

        // TODO: Additionally, for each state, the lemonImage should be set to the corresponding
        //  drawable from the drawable resources. The drawables have the same names as the strings
        //  but remember that they are drawables, not strings.
        //DICH
        // VIEC CAN LAM: Ngoài ra, đối với mỗi trạng thái, LemonImage phải được đặt thành tương ứng
        // có thể vẽ được từ các tài nguyên có thể vẽ được. Các vật có thể rút ra có cùng tên với các chuỗi
        // nhưng hãy nhớ rằng chúng là có thể kéo được, không phải chuỗi.

    }

    /**
     * === KHÔNG THÊM PHƯƠNG PHÁP NÀY ===
     *
     * Nhấp và giữ hình ảnh quả chanh sẽ hiển thị số lần quả chanh đã được vắt.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * Lớp cây chanh với phương pháp "hái" quả chanh. "Kích thước" của quả chanh được xếp ngẫu nhiên
 * và xác định số lần một quả chanh cần được vắt trước khi bạn có được nước chanh.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
