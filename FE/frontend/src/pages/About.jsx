import React from 'react'
import { assets } from '../assets/assets'

const About = () => {
  return (
    <div>

      <div className='text-center text-2xl pt-10 text-[#707070]'>
        <p>ABOUT <span className='text-gray-700 font-semibold'> US</span></p>
      </div>

      <div className='my-10 flex flex-col md:flex-row gap-12'>
        <img className='w-full md:max-w-[360px]' src={assets.about_image} alt="" />
        <div className='flex flex-col justify-center gap-6 md:w-2/4 text-sm text-gray-600'>
          <p>Chào mừng bạn đến với Phòng Khám Răng Miệng Hust, đối tác tin cậy của bạn trong việc quản lý nhu cầu chăm sóc sức khỏe răng miệng một cách tiện lợi và hiệu quả. Tại Phòng Khám Răng Miệng Hust, chúng tôi hiểu những thách thức mà mọi người gặp phải khi lên lịch hẹn với bác sĩ và quản lý hồ sơ sức khỏe răng miệng của mình.</p>
          <p>HUST DENTAL CLINIC cam kết mang lại chất lượng xuất sắc trong chăm sóc sức khỏe răng miệng. Chúng tôi không ngừng cải tiến dịch vụ, tích hợp các tiến bộ mới nhất để nâng cao trải nghiệm của bệnh nhân và cung cấp sự chăm sóc vượt trội. Dù bạn đang đặt lịch hẹn lần đầu tiên hay quản lý các điều trị lâu dài, Phòng Khám Răng Miệng Hust luôn đồng hành cùng bạn.</p>
          <b className='text-gray-800'>Tầm Nhìn Của Chúng Tôi</b>
          <p>Tầm nhìn của chúng tôi tại Phòng Khám Răng Miệng Hust là tạo ra một trải nghiệm chăm sóc sức khỏe răng miệng liền mạch cho mọi bệnh nhân. Chúng tôi mong muốn thu hẹp khoảng cách giữa bệnh nhân và các chuyên gia nha khoa, giúp bạn dễ dàng tiếp cận dịch vụ chăm sóc khi bạn cần.</p>
        </div>
      </div>

      <div className='text-xl my-4'>
        <p><span className='text-gray-700 font-semibold'>WHY CHOOSE US</span></p>
      </div>

      <div className='flex flex-col md:flex-row mb-20'>
        <div className='border px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-primary hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>HIỆU QUẢ:</b>
          <p>Hệ thống đặt lịch hẹn nhanh chóng, phù hợp với lịch trình bận rộn của bạn.</p>
        </div>
        <div className='border px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-primary hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>TIỆN LỢI:</b>
          <p>Truy cập mạng lưới các bác sĩ nha khoa uy tín trong khu vực của bạn.</p>
        </div>
        <div className='border px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-primary hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>CÁ NHÂN HÓA:</b>
          <p>Các đề xuất và nhắc nhở được cá nhân hóa giúp bạn theo dõi sức khỏe răng miệng của mình.</p>
        </div>
      </div>

    </div>
  )
}

export default About
