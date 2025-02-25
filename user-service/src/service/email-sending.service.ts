/**
 * Purpose:
 * Service to send emails to the users
 */
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { createTransport, SendMailOptions, Transporter } from 'nodemailer';

@Injectable()
export class EmailService {
  private mailTransport: Transporter;

  constructor(private configService: ConfigService) {
    this.mailTransport = createTransport({
      host: 'Gmail',
      port: 465,
      secure: true,
      auth: {
        user: this.configService.get('SMTP_ID'),
        pass: this.configService.get('SMTP_PW'),
      },
    });
  }

  async sendEmail(data: SendEmailDto): Promise<{ success: boolean } | null> {
    const { sender, recipients, subject, html, text } = data;

    const mailOptions: SendMailOptions = {
      from: sender ?? {
        name: this.configService.get('MAIL_SENDER_NAME'),
        address: this.configService.get('MAIL_SENDER_EMAIL'),
      },
      to: recipients,
      subject,
      html,
      text,
    };

    try {
      await this.mailTransport.sendMail(mailOptions);
      return { success: true };
    } catch (err: unknown) {
      if (err instanceof Error) {
        console.log('Email sending error: ', err.message);
      } else {
        console.log('Unexpecter error: ', err);
      }
      return null;
    }
  }
}
